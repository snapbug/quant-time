import org.warcbase.spark.matchbox._
import org.warcbase.spark.matchbox.TweetUtils._
import org.warcbase.spark.rdd.RecordRDD._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.text.SimpleDateFormat
import java.util.TimeZone
import org.apache.commons.lang.StringEscapeUtils;
 
val tweets = RecordLoader.loadTweets("/collections/tweets/Tweets2013/", sc)

val shortTweets = tweets.filter(tweet => (tweet \ "id_str") != JNothing)
                      .flatMap(tweet => {
                        implicit val formats = DefaultFormats
                        val tweetID = (tweet \ "id_str").extract[String]
                        val tweetCreatedAt = (tweet \ "created_at").extract[String]
                        val tweetText = (tweet \ "text").extract[String]

                        val dateInFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
                        dateInFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                        val dateOutFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        dateOutFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                        val parsedCreatedAt =  {
                            try { 
                                dateOutFormat.format(dateInFormat.parse(tweetCreatedAt)) 
                            } 
                            catch { case e: Exception => null }
                        }
                        if(parsedCreatedAt != null)
                            List( (tweetID, parsedCreatedAt, tweetText) )
                        else
                            List()
                      })

// shortTweets.take(20).foreach(println)
shortTweets.persist()
          
// Feb till 28, March till 31
for( month <- 3 to 3) {
    for( day <- 1 to 31) {
        val monthStr = "%02d".format(month)
        val dayStr = "%02d".format(day)
        val dateStr = "2013-" + monthStr + "-" + dayStr
        println( "***SALMAN*** Starting date: " + dateStr);

        shortTweets.filter(tweetTuple => {
                        val tweetCreatedAt = tweetTuple._2
                        val dateInFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val dateOutFormat = new SimpleDateFormat("yyyy-MM-dd")
                        val parsedCreatedAt =  {
                            try { dateOutFormat.format(dateInFormat.parse(tweetCreatedAt)) } 
                            catch { case e: Exception => null }
                        }
                        ( (parsedCreatedAt != null) && parsedCreatedAt.equals(dateStr) )
                    })
                    .distinct()
                    .sortBy(tweetTuple => { (tweetTuple._2, tweetTuple._1, tweetTuple._3) })
                    .map(tweetTuple => {
                        String.format("<DOC id=\"%s\"><timestamp=%s />%s</DOC>", tweetTuple._1, tweetTuple._2, tweetTuple._3);
                    })
                    .saveAsTextFile("Tweets2013-" + monthStr + "-" + dayStr)

        println( "***SALMAN*** Finished date: " + dateStr + "\n\n\n");
    }
}

