# myMD code

Java based high-performance generic molecular dynamics simulation code supporting
standard gromacs input files


## Requirements

Parallel computing algorithm is implemented using MPJ-Express library(http://mpj-express.org),
and Particle-Mesh-Ewald summation for fast long-range interaction requires FFT library 
provided by JTransforms(https://sites.google.com/site/piotrwendykier/software/jtransforms).


## Usage Example
To run a regular MD simulation on a single core machine.

    RunSkeleton [prm file] [gro file] [top file] [out traj] [out energy]


To run a regular MD simulation on a distributed memory multi-core/multi-node machone.

    mpjrun.sh -np [number of cores] MpiRunSkeleton [prm file] [gro file] [top file] [out traj] [out energy]



## Features
### Domain-based decomposition for efficient Parallel Computing
parallel computation on a distrubted memory system(e.g. cluster computer) is supported through MPJ express library. Current version supports domain-based decomposition which results in better scaling performance compared to particle-based decomposition.
### Compatible with standard Gromacs input files
can use standard Gromacs like input files (e.g. *.gro, *.itp, *.top) allowing one to use this code to complement package programs.
### Customizable
unlike package programs, myMD code is developed as a flexible tool to suit various different types of simulations. Document explaining full API will be provided in the near future.

