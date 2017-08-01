#!/bin/bash

index="stdbuf -oL ./atire/bin/index -rrwarcgz -sa -iscrub:un"
search="./atire/bin/atire -k1000 -l0 -et -RBM25 -QN:t -sa"
cw09b="/collections/ClueWeb09b/ClueWeb09_English_1"

## Index the segments at each bit level
for s in ${cw09b}/en00*
do
	for q in `seq 2 1 16`
	do
		bn=$(basename ${s})
		set -o noglob
		${index} -QBM25 -q${q} ${s}/*warc.gz | tee -a indexing-cw09b-s${bn}.txt
		set +o noglob
		ls -l index.aspt | tee -a indexing-cw09b-s${bn}.txt

		for queries in "wt2010-topics.xml" "full-topics.xml"
		do
			${search} -q${queries} -o${queries}-s${bn}-q${q}.run  | ${queries}-s${bn}-q${q}.timings
		done
		rm -f index.aspt
	done
done
