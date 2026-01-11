package paige.subsonic.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
	val album: Album
)

@Serializable
data class AlbumListResponse(
	val albumList: AlbumList
)

@Serializable
data class AlbumList2Response(
	val albumList2: AlbumList
)

@Serializable
data class AlbumList(
	val album: List<Album>?
)

@Serializable
data class Album(
	override val coverArt: String?,
	override val duration: Int?,
	override val id: String,
	val artist: String?,
	val artistId: String?,
	val created: String,
	val genre: String?,
	val name: String?,
	val album: String?,
	val playCount: Int?,
	val song: List<Song>?,
	val songCount: Int?,
	val year: Int?,
	val userRating: Int?
) : TrackCollection {
	override val title: String?
		get() = name

	override val subtitle: String?
		get() = artist

	override val tracks: List<Track>
		get() = song.orEmpty()

	override val trackCount: Int
		get() = songCount ?: song?.count() ?: -1
}

@Serializable
data class SearchResult3Response(
	val searchResult3: SearchResult3
)

@Serializable
data class SearchResult3(
	val song: List<Song>?,
	val album: List<Album>?,
	val artist: List<Artist>?
)
