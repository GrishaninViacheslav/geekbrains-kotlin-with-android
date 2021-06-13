package GeekBrians.Slava_5655380.domain.model.tmdbrepository

data class TmdbDTO(val results: Array<TmdbDTOResult> = Array<TmdbDTOResult>(0) { TmdbDTOResult() })

data class TmdbDTOResult(val original_title: String = "", val poster_path: String = "")