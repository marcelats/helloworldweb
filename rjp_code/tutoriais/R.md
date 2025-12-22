# Running the simulation codes generated in R
Installing dependencies to run simulation codes.

## Dependecies ##
- R => 4.0.1
- Simmer => 4.4.5
## Install Dependecies

1 - Update the repositories with the command:
```bash
sudo apt-get update      
```
2 - Install R with the command:
```bash
sudo apt-get install r-base     
```
2 - Enter the R terminal with the command:
```bash
R     
```
3 - Install Simmer in the R terminal with the command:
```bash
install.packages("simmer")     
```

## Runing the Code

3 - Execute "R CMD BATCH" command with file name:
```bash
  R CMD BATCH Mm1Simmer.R
```


## References ##
- https://r-simmer.org/
- https://www.r-project.org/
