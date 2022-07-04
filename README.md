# Binomial-Surface-Combat-Simulator
A Java program that simulates naval surface combat between two forces to produce data of different force composition performances.

The program was originally written as a more detailed but easy to use version to substitute Salvo Model, Wayne P. Hughes Jr. deterministic model published in March 1995: https://doi.org/10.1002/1520-6750(199503)42:2%3C267::AID-NAV3220420209%3E3.0.CO;2-Y

Researchers have updated the Salvo Model several times, including a Stochastic Salvo Model for Naval Surface Combat by Michael J. Armstrong, available at https://www.jstor.org/stable/25146917

However, while the simplicity is the beauty of these models and promotes Occam's razors principle, the simplicity is often deemed as necessary due to the ease of use, and disregards important aspects that are integrated into the tactical and battle technical use of anti-ship missiles, such as fitting the fired salvo to the size or type of the opponent. It was also deemed unnecessary, in the end, to differentiate the survivability from a strike, as research and history shows that if an anti-ship missile hits its target, the target is most likely at least out of operations, if not sunk. 

With above mentioned points in mind, the program was written to simulate a surface warfare encounter between two parties, using binomial probability to determine the success of a missile engagement. At first, the program allocates engagements to units which have missiles left to use. Then, by a random draw, it is determined wether the target spots the engagement in time. The spotting effects the probability of successful engagement in accordance with "An analysis of the historical effectiveness of anti-ship cruise missiles in littoral warfare" by John C. Schulte, available at https://calhoun.nps.edu/handle/10945/27962.
These probabilities are used, depending on the random draw, to calculate the probability of success in a binomial distribution with the particular salvo size (n) and drawing another uniform random number to determine if the engagement results in a hit. 

The program has a simple text interface that can be used to produce quick simulations for a determined number of iterations, preferably over 100 at a time. The execution is fairly fast up to 10 000 iterations even for a multitude of force composition options.

For now, the opposing force composition is static as are the combatant features. However, these can be tweaked as desired in the code itself to test different set ups. 
