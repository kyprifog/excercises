import com.spotify.scio.ScioContext
import io.circe._
import io.circe.parser._

import scala.util.Try

object Songs {
  case class Like(user_id: Long, song_id: Long, dislike: Boolean, time: Long) {
    val key: (Long, Long) = (user_id, song_id)
  }

  object Mood extends Enumeration {
    val calm = Value("calm")
    val pensive = Value("pensive")
    val moving = Value("moving")
  }
  case class Song(song_id: Long, name: String, album_id: Long, mood: Mood.Value)
  case class Album(album_id: Long, genre: Genre.Value, subgenre: SubGenre.Value)

  case object Genre extends Enumeration {
    val classical = Value("classical")
    val rock = Value("rock")
  }

  case object SubGenre extends Enumeration {
    val classicalPiano = Value("classical piano")
    val alternativeRock = Value("alternative rock")
    val folkRock = Value("folk rock")
  }

  def run(likesPath: String, songsPath: String, albumsPath: String) = {
    val sc = ScioContext()
    implicit val decodeLike: Decoder[Like] = Decoder.forProduct4("user_id", "song_id", "dislike", "time")(Like.apply)

    def getLike(s: String): Option[Like] = {
      decode[Like](s) match {
        case Left(e) => { println(e.getMessage); val n: Option[Like] = None; n}
        case Right(l) => Some(l)
      }
    }

    def getSong(s: String): Option[Song] = {
      Try{
        val splitted = s.split(",").map(_.trim).toList
        val song_id = splitted(0).toLong
        val name = splitted(1)
        val album_id = splitted(2).toLong
        val mood = Mood.withName(splitted(3))
        Song(song_id, name, album_id, mood)
      }.toOption
    }

    def getAlbum(s: String): Option[Album] = {
      Try{
        val splitted = s.split(",").map(_.trim).toList
        val album_id = splitted(0).toLong
        val genre = Genre.withName(splitted(1))
        val subgenre = SubGenre.withName(splitted(2))
        Album(album_id, genre, subgenre)
      }.toOption
    }

    val likes = sc.textFile(likesPath).flatMap(getLike).map{l => (l.song_id, l)}
    val songs = sc.textFile(songsPath).flatMap(getSong).distinctBy(_.song_id).map{s => (s.album_id, s)}
    val albums = sc.textFile(albumsPath).flatMap(getAlbum).distinctBy(_.album_id).map{a => (a.album_id, a)}

    val joined = likes
      .leftOuterJoin{
        songs.leftOuterJoin(albums).map{case(album_id, (song, album)) =>
          (song.song_id, (song, album))
        }}
      .mapValues { r =>
        val like = r._1
        val song = r._2.map { s => s._1 }
        val album = r._2.flatMap { s => s._2 }
        (like, song, album)
      }
      .values

    joined
      .map{case(like, song, album) => (like.key, (like, song, album))}
      .groupByKey
      .flatMapValues{r =>
        r.toList.sortBy(-_._1.time).headOption
      }
      .mapKeys(_._1)
      .groupByKey
      .mapValues{r =>
        val subGenres = r
          .filter{r2 => !r2._1.dislike}
          .flatMap{r1 => r1._3.map{r2 => r2.subgenre}}
        subGenres.groupBy(identity).mapValues(_.size)
      }
      .tap{case(user_id, map) =>
        var message = ""
        message += "-"*50 + "\n"
        message += s"User: ${user_id}" + "\n"
        message += "-"*50 + "\n"
        for (m <- map) {
          message += f"${m._1}: ${m._2}\n"
        }
        message += "-"*50 + "\n"
        println(message)
      }

    val result = sc.run().waitUntilFinish()
    result


  }

}

val path = "/Users/kyle/dev/excercises/data/"
val results = Songs.run(path + "likes.jsonl", path+"songs.txt", path+"albums.txt")

// STDOUT:
//    --------------------------------------------------
//    user: 2
//    --------------------------------------------------
//    folk rock: 1
//    --------------------------------------------------
//
//    --------------------------------------------------
//    user: 5
//    --------------------------------------------------
//    folk rock: 2
//    classical piano: 1
//    --------------------------------------------------
//
//    --------------------------------------------------
//    user: 4
//    --------------------------------------------------
//    folk rock: 1
//    classical piano: 1
//    --------------------------------------------------
//
//    --------------------------------------------------
//    user: 3
//    --------------------------------------------------
//    alternative rock: 1
//    --------------------------------------------------
//
//    --------------------------------------------------
//    user: 1
//    --------------------------------------------------
//    folk rock: 1
//    alternative rock: 1
//    --------------------------------------------------

println("DONE")
