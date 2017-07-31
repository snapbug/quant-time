#!/bin/bash

index="stdbuf -oL ./atire/bin/index -rrtrec -sa -iscrub:un"
search="./atire/bin/atire -k1000 -l0 -et -Rlmjm -qtopics"

## Index the whole collection at the different bit levels
## Figure 1
for q in `seq 2 1 16`
do
	${index} -Qlljm -q${q} Tweets2013* | tee -a indexing-tweets2013-q${q}.txt
	ls -l index.aspt | tee -a indexing-tweets2013-q${q}.txt

	for queries in "tweet-topics"
	do
		${search} -o${queries}-complete-q${q}.run 
	done
	rm -f index.aspt
done

## Index the week collection
## Figure 2, 3a
for w in `seq 0 1 8`
do
	${index} Tweets2013-w${w}* | tee -a indexing-tweets2013-w${w}-q-.txt
	ls -l index.aspt | tee -a indexing-tweets2013-w${w}-q-.txt

	for queries in "tweet-topics"
	do
		${search} -o${queries}-w${w}-q-.run 
	done
	rm -f index.aspt

	## And at the bit levels
	for q in `seq 2 1 16`
	do
		${index} -Qlljm -q${q} Tweets2013* | tee -a indexing-tweets2013-w${w}-q${q}.txt
		ls -l index.aspt | tee -a indexing-tweets2013-w${w}-q${q}.txt

		for queries in "tweet-topics"
		do
			${search} -o${queries}-w${w}-q${q}.run | tee -a -o${queries}-w${w}-q${q}.timings
		done
		rm -f index.aspt
	done
done
