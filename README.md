# Binomial-Surface-Combat-Simulator

## Introduction

This is a Java program that simulates naval surface combat between two forces to produce data of different force composition performances. It was originally written as a part of an operations research project aimed at estimating the warfighting benefits of introducing unmanned surface vehicles (USVs) into littoral surface action group compositions.

To produce results to support the multi-criteria decision analysis on the benefits of unmanned platforms, the program was written as a more detailed but easy to use upgrade on the *Salvo Model* by Wayne P. Hughes Jr., which is a *deterministic model* published in March 1995: https://doi.org/10.1002/1520-6750(199503)42:2%3C267::AID-NAV3220420209%3E3.0.CO;2-Y

Researchers have updated the Salvo Model several times since, including a *Stochastic Salvo Model* for Naval Surface Combat by Michael J. Armstrong, available at https://www.jstor.org/stable/25146917

However, while the simplicity is the beauty of these models and promotes Occam's razors principle, the simplicity is often deemed as necessary due to the ease of use (giving regard to spreadsheet calculations and such), and disregards important aspects that are integrated into the tactical and battle technical use of anti-ship missiles, such as fitting the fired salvo to the size or type of the opponent. As such limitations are rarely valid these days, as the purpose here was to create more and more accurate data on different force compositions, the simpler models were too rough around the edges whereas more complex simulators such as actual naval combat games were, for now, beyond this particular researchers reach of expertise. While the allocation of engagements and use of missiles was made more intelligent (fitting resources to strike while avoiding overkills) it was also deemed unnecessary to differentiate the survivability from a strike, as research and history shows that if an anti-ship missile hits its target, the target is most likely at least out of operations, if not sunk. Instead of static survivability factor (i.e. ability to overcome x number of incoming missiles) it would be for the better to use actual (planned) salvo sizes against each opposing unit type separately. 

With above mentioned points in mind, the program was written to simulate a surface warfare encounter between two parties, using binomial probability to determine the success of a missile engagement. At first, the program allocates engagements to units which have missiles left to use. Then, by a random draw, it is determined wether the target spots the engagement in time. The spotting effects the probability of successful engagement in accordance with "An analysis of the historical effectiveness of anti-ship cruise missiles in littoral warfare" by John C. Schulte, available at https://calhoun.nps.edu/handle/10945/27962.
These probabilities are used, depending on the random draw, to calculate the probability of success in a binomial distribution with the particular salvo size (n) and drawing another uniform random number to determine if the engagement results in a hit. 

The program also penalizes the reliability of unmanned units by introducing stochastic randomization towards the availability of anti-ship capable USVs with a random draw from Beta(1,3) distribution with the threshold of > 0.5 in order to be able to participate in an engagement. As the mean of Beta(1,3) is 0.25, this greatly lessens the effectiveness of USVs in these simulations. The purpose is to even out the otherwise trivially obvious result, that more hulls and more missiles usually results in more desolation of the opposing force, as USVs lack the ability to apply human decision making and reasoning in the case of comms breakdown and other prominent factors that are ever present in a warfighting scenario.

## Use

The program has a simple text interface that can be used to produce quick simulations for a determined number of iterations, preferably over 100 at a time. The execution is fairly fast up to 10 000 iterations even for a multitude of force composition options.

For now, the opposing force composition is static as are the combatant features. However, these can be tweaked as desired in the code itself to test different set ups. 
