package com.souzaemerson.marvelproject.data.model

fun converterToFavorite(results: Results) = Favorites(
    id = results.id,
    name = results.name,
    description = results.description,
    modified = results.modified,
    thumbnail = results.thumbnail,
    resourceURI = results.resourceURI,
    comics = results.comics,
    series = results.series,
    stories = results.stories,
    events = results.events,
    urls = results.urls,
    email = ""
)

fun converterToResult(favorites: List<Favorites>): MutableList<Results> {
    val results = mutableListOf<Results>()
    favorites.forEach {
        results.add(Results(
            id = it.id,
            name = it.name,
            description = it.description,
            modified = it.modified,
            thumbnail = it.thumbnail,
            resourceURI = it.resourceURI,
            comics = it.comics,
            series = it.series,
            stories = it.stories,
            events = it.events,
            urls = it.urls
        ))
    }
    return results
}