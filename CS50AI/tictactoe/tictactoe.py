"""
Tic Tac Toe Player
"""

import math
from copy import deepcopy

X = "X"
O = "O"
EMPTY = None


def initial_state():
    """
    Returns starting state of the board.
    """
    return [[EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY]]


def player(board):
    """
    Returns player who has the next turn on a board.
    """
    count = 0

    for row in board:
        for cell in row:
            if cell == X:
                count += 1
            elif cell == O:
                count -= 1

    if count == 0:
        return X
    else:
        return O


def actions(board):
    """
    Returns set of all possible actions (i, j) available on the board.
    """
    possible_actions = set()

    for i, row in enumerate(board):
        for j, cell in enumerate(row):
            if cell == EMPTY:
                possible_actions.add((i, j))

    return possible_actions


def result(board, action):
    """
    Returns the board that results from making move (i, j) on the board.
    """
    (i, j) = action

    if board[i][j] != EMPTY:
        raise Exception("You must choose an empty cell.")

    hipothetical_board = deepcopy(board)

    hipothetical_board[i][j] = player(board)

    return hipothetical_board


def winner(board):
    """
    Returns the winner of the game, if there is one.
    """
    for i in range(3):
        if board[i][0] != EMPTY and board[i][0] == board[i][1] and board[i][1] == board[i][2]:
            return board[i][0]
        if board[0][i] != EMPTY and board[0][i] == board[1][i] and board[1][i] == board[2][i]:
            return board[0][i]

    if board[0][0] != EMPTY and board[0][0] == board[1][1] and board[1][1] == board[2][2]:
        return board[0][0]

    if board[0][2] != EMPTY and board [0][2] == board[1][1] and board[1][1] == board[2][0]:
        return board[0][2]
    
    return None


def terminal(board):
    """
    Returns True if game is over, False otherwise.
    """
    if winner(board) != None:
        return True
    
    for row in board:
        for cell in row:
            if cell == EMPTY:
                return False
    
    return True


def utility(board):
    """
    Returns 1 if X has won the game, -1 if O has won, 0 otherwise.
    """
    the_winner = winner(board)

    if the_winner == None: return 0
    if the_winner == "X": return 1
    if the_winner == "O": return -1


def minimax(board):
    """
    Returns the optimal action for the current player on the board.
    """
    if terminal(board): return None

    optimal_action = None
    
    if player(board) is X:
        optimal_action = max_value(board)['action']
    else:
        optimal_action = min_value(board)['action']

    return optimal_action


def min_value(board):
    if terminal(board): return { 'value':utility(board), 'action': None }

    v = math.inf
    optimal_action = None

    for action in actions(board):
        possible_result = max_value((result(board, action)))['value']

        if v > possible_result:
            v = possible_result
            optimal_action = action
        
    return { 'value':v, 'action':optimal_action }
    

def max_value(board):
    if terminal(board): return { 'value':utility(board), 'action': None }

    v = -math.inf
    optimal_action = None

    for action in actions(board):
        possible_result = min_value((result(board, action)))['value']

        if v < possible_result:
            v = possible_result
            optimal_action = action
    
    return { 'value':v, 'action':optimal_action }
