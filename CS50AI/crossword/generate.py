import sys
from collections import deque

from crossword import *


class CrosswordCreator():

    def __init__(self, crossword: Crossword):
        """
        Create new CSP crossword generate.
        """
        self.crossword = crossword
        self.domains = {
            var: self.crossword.words.copy()
            for var in self.crossword.variables
        }

    def letter_grid(self, assignment):
        """
        Return 2D array representing a given assignment.
        """
        letters = [
            [None for _ in range(self.crossword.width)]
            for _ in range(self.crossword.height)
        ]
        for variable, word in assignment.items():
            direction = variable.direction
            for k in range(len(word)):
                i = variable.i + (k if direction == Variable.DOWN else 0)
                j = variable.j + (k if direction == Variable.ACROSS else 0)
                letters[i][j] = word[k]
        return letters

    def print(self, assignment):
        """
        Print crossword assignment to the terminal.
        """
        letters = self.letter_grid(assignment)
        for i in range(self.crossword.height):
            for j in range(self.crossword.width):
                if self.crossword.structure[i][j]:
                    print(letters[i][j] or " ", end="")
                else:
                    print("█", end="")
            print()

    def save(self, assignment, filename):
        """
        Save crossword assignment to an image file.
        """
        from PIL import Image, ImageDraw, ImageFont
        cell_size = 100
        cell_border = 2
        interior_size = cell_size - 2 * cell_border
        letters = self.letter_grid(assignment)

        # Create a blank canvas
        img = Image.new(
            "RGBA",
            (self.crossword.width * cell_size,
             self.crossword.height * cell_size),
            "black"
        )
        font = ImageFont.truetype("assets/fonts/OpenSans-Regular.ttf", 80)
        draw = ImageDraw.Draw(img)

        for i in range(self.crossword.height):
            for j in range(self.crossword.width):

                rect = [
                    (j * cell_size + cell_border,
                     i * cell_size + cell_border),
                    ((j + 1) * cell_size - cell_border,
                     (i + 1) * cell_size - cell_border)
                ]
                if self.crossword.structure[i][j]:
                    draw.rectangle(rect, fill="white")
                    if letters[i][j]:
                        _, _, w, h = draw.textbbox((0, 0), letters[i][j], font=font)
                        draw.text(
                            (rect[0][0] + ((interior_size - w) / 2),
                             rect[0][1] + ((interior_size - h) / 2) - 10),
                            letters[i][j], fill="black", font=font
                        )

        img.save(filename)

    def solve(self):
        """
        Enforce node and arc consistency, and then solve the CSP.
        """
        self.enforce_node_consistency()
        self.ac3()
        return self.backtrack(dict())

    def enforce_node_consistency(self):
        """
        Update `self.domains` such that each variable is node-consistent.
        (Remove any values that are inconsistent with a variable's unary
         constraints; in this case, the length of the word.)
        """
        for var, domain in self.domains.items():
            for word in domain.copy():
                if len(word) != var.length:
                    domain.remove(word)

    def revise(self, x, y):
        """
        Make variable `x` arc consistent with variable `y`.
        To do so, remove values from `self.domains[x]` for which there is no
        possible corresponding value for `y` in `self.domains[y]`.

        Return True if a revision was made to the domain of `x`; return
        False if no revision was made.
        """
        if (x, y) not in self.crossword.overlaps:
            return False
        
        x_index, y_index = self.crossword.overlaps[x, y]
        x_domain, y_domain = self.domains[x], self.domains[y]

        revised = False
        acceptable_chars = {word[y_index] for word in y_domain}

        for word in x_domain.copy():
            if word[x_index] not in acceptable_chars:
                x_domain.remove(word)
                revised = True
        
        return revised


    def ac3(self, arcs=None):
        """
        Update `self.domains` such that each variable is arc consistent.
        If `arcs` is None, begin with initial list of all arcs in the problem.
        Otherwise, use `arcs` as the initial list of arcs to make consistent.
        """
        if arcs is None:
            arcs = deque()
            for var in self.crossword.variables:
                for neighbor in self.crossword.neighbors(var):
                    arcs.append((var, neighbor))
        else:
            arcs = deque(arcs)

        """
        Return True if arc consistency is enforced and no domains are empty;
        return False if one or more domains end up empty.
        """
        while arcs:
            arc = arcs.popleft()
            if self.revise(arc[0], arc[1]):
                for neighbor in self.crossword.neighbors(arc[0]):
                    arcs.append((neighbor, arc[0]))
        
        for domain in self.domains.values():
            if not domain:
                return False
        return True


    def assignment_complete(self, assignment):
        """
        Return True if `assignment` is complete (i.e., assigns a value to each
        crossword variable); return False otherwise.
        """
        for var in self.crossword.variables:
            if not var in assignment or not assignment[var]:
                return False
        return True
    

    def consistent(self, assignment):
        """
        Return True if `assignment` is consistent (i.e., words fit in crossword
        puzzle without conflicting characters); return False otherwise.
        """
        used_words = set()
        for var, word in assignment.items():
            if word in used_words or len(word) != var.length:
                return False
            used_words.add(word)

            for neighbor in self.crossword.neighbors(var):
                var_index, neighbor_index = self.crossword.overlaps[var, neighbor]
                if neighbor in assignment and (word[var_index] != assignment[neighbor][neighbor_index]):
                    return False
        
        return True

    def order_domain_values(self, var, assignment: dict):
        """
        Return a list of values in the domain of `var`, in order by
        the number of values they rule out for neighboring variables.
        The first value in the list, for example, should be the one
        that rules out the fewest values among the neighbors of `var`.
        """
        neighbors = self.crossword.neighbors(var) - set(assignment.keys())
        ruled_out = {word: 0 for word in self.domains[var]}

        for word in ruled_out:
            for n in neighbors:
                var_index, n_index = self.crossword.overlaps[var, n]
                for n_word in self.domains[n]:
                    if word[var_index] != n_word[n_index]:
                        ruled_out[word] += 1

        return sorted(ruled_out, key=ruled_out.get)

    def select_unassigned_variable(self, assignment: dict):
        """
        Return an unassigned variable not already part of `assignment`.
        Choose the variable with the minimum number of remaining values
        in its domain. If there is a tie, choose the variable with the highest
        degree. If there is a tie, any of the tied variables are acceptable
        return values.
        """
        min_var, mrv, num_neighbors = None, float('inf'), 0

        for var in (self.crossword.variables - set(assignment.keys())):
            remaining_variables = len(self.domains[var])
            if (remaining_variables > mrv) or (remaining_variables == mrv and num_neighbors > len(self.crossword.neighbors(var))):
                continue
            min_var, mrv, num_neighbors = var, len(self.domains[var]), len(self.crossword.neighbors(var))
        
        return min_var

    def backtrack(self, assignment: dict):
        """
        Using Backtracking Search, take as input a partial assignment for the
        crossword and return a complete assignment if possible to do so.

        `assignment` is a mapping from variables (keys) to words (values).

        If no assignment is possible, return None.
        """
        if self.assignment_complete(assignment):
            return assignment
        
        var = self.select_unassigned_variable(assignment)
        for word in self.domains[var]:
            assignment[var] = word
            if self.consistent(assignment):
                arcs = [(n, var) for n in self.crossword.neighbors(var)]
                self.ac3(arcs)
                result = self.backtrack(assignment)
                if result:
                    return result
            del assignment[var]
        return None

def main():

    # Check usage
    if len(sys.argv) not in [3, 4]:
        sys.exit("Usage: python generate.py structure words [output]")

    # Parse command-line arguments
    structure = sys.argv[1]
    words = sys.argv[2]
    output = sys.argv[3] if len(sys.argv) == 4 else None

    # Generate crossword
    crossword = Crossword(structure, words)
    creator = CrosswordCreator(crossword)
    assignment = creator.solve()

    # Print result
    if assignment is None:
        print("No solution.")
    else:
        creator.print(assignment)
        if output:
            creator.save(assignment, output)


if __name__ == "__main__":
    main()
