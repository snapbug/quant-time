# Quantization in Append-Only Collections

`exploreTweets2013.scala` is a scala script that will split the Tweets2013 collection into the weekly splits that were used in the paper. Change line 10 to point to the folder that contains your Tweet2013 collection. The outcome will be a selection of `.txt` files that contain tweets in a format that can be suitably indexed by ATIRE.

Run `setup.sh` which will clone the version of ATIRE used, and build it.

Run the `tweets.sh` script. This will create all the indexes, and perform all the runs. The indexes are destroyed as they are used, as they consume a substantial amount of diskspace.

Finally, the `cw09b.sh` script, which will index and search the cw09b segments individually.

At the end, the `*timings` files can be parsed for millisecond query timings, the index logs will have the index sizes, and the `*run` files can be processed by `trec_eval` to get metrics.
