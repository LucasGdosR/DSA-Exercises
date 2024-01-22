import os
import random
import re
import sys
from copy import deepcopy

DAMPING = 0.85
SAMPLES = 10000


def main():
    if len(sys.argv) != 2:
        sys.exit("Usage: python pagerank.py corpus")
    corpus = crawl(sys.argv[1])
    ranks = sample_pagerank(corpus, DAMPING, SAMPLES)
    print(f"PageRank Results from Sampling (n = {SAMPLES})")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")
    ranks = iterate_pagerank(corpus, DAMPING)
    print(f"PageRank Results from Iteration")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")


def crawl(directory):
    """
    Parse a directory of HTML pages and check for links to other pages.
    Return a dictionary where each key is a page, and values are
    a list of all other pages in the corpus that are linked to by the page.
    """
    pages = dict()

    # Extract all links from HTML files
    for filename in os.listdir(directory):
        if not filename.endswith(".html"):
            continue
        with open(os.path.join(directory, filename)) as f:
            contents = f.read()
            links = re.findall(r"<a\s+(?:[^>]*?)href=\"([^\"]*)\"", contents)
            pages[filename] = set(links) - {filename}

    # Only include links to other pages in the corpus
    for filename in pages:
        pages[filename] = set(
            link for link in pages[filename]
            if link in pages
        )

    return pages


def transition_model(corpus, page, damping_factor):
    """
    Return a probability distribution over which page to visit next,
    given a current page.

    With probability `damping_factor`, choose a link at random
    linked to by `page`. With probability `1 - damping_factor`, choose
    a link at random chosen from all pages in the corpus.
    """
    if not corpus[page]:
        random_probability = 1 / len(corpus)
    else:     
        random_probability = (1 - damping_factor) / len(corpus)
    
    prob_distribution = {p: random_probability for p in corpus}
    
    linked_pages = corpus[page]
    for p in linked_pages:
        prob_distribution[p] += damping_factor / len(linked_pages)
    
    return prob_distribution


def sample_pagerank(corpus, damping_factor, n):
    """
    Return PageRank values for each page by sampling `n` pages
    according to transition model, starting with a page at random.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    pagerank = {p: 0 for p in corpus}
    
    page = random.choice(list(corpus.keys()))

    scaled_visit = 1 / n

    for _ in range(n):
        pagerank[page] += scaled_visit
        model = transition_model(corpus, page, damping_factor)
        rand = random.random()
        acc = 0
        for k, v in model.items():
            acc += v
            if acc > rand:
                page = k
                break
    
    return pagerank


def iterate_pagerank(corpus, damping_factor):
    """
    Return PageRank values for each page by iteratively updating
    PageRank values until convergence.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    rank = 1 / len(corpus)
    pagerank = {p: rank for p in corpus}

    rev_corpus = {p: [] for p in corpus}
    for k, v in corpus.items():
        for p in v:
            rev_corpus[p].append(k)

    change = 1
    const = (1 - damping_factor) / len(corpus)

    while change > 0.001:
        change = 0
        copy = deepcopy(pagerank)
        for k, v in copy.items():
            var = 0
            for p in rev_corpus[k]:
                var += copy[p] / len(corpus[p])
            pagerank[k] = const + damping_factor * var
            change = max(change, abs(v - pagerank[k]))

    return pagerank


if __name__ == "__main__":
    main()
