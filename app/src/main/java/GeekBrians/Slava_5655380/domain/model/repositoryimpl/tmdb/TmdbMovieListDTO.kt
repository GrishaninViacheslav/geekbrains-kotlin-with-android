package GeekBrians.Slava_5655380.domain.model.repositoryimpl.tmdb

data class TmdbMovieListDTO(val results: Array<TmdbMovieDTO> = Array<TmdbMovieDTO>(0) { TmdbMovieDTO() })

data class TmdbMovieDTO(val id: Int = -1, val original_title: String = "", val poster_path: String = "")