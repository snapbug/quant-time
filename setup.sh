#!/bin/bash

hg clone http://www.atire.org/hg/atire atire -r 693e62907028
make -C atire

curl -O http://trec.nist.gov/data/web/10/wt2010-topics.xml
curl -O http://trec.nist.gov/data/web/11/full-topics.xml
