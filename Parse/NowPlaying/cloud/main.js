var _ = require('underscore');

Parse.Cloud.define("inTheaters", function(request, response) {
	var timestamp = new Date().getTime();

	Parse.Cloud.httpRequest({
		url: 'http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=16&country=us&apikey=qdcwaccyw2tbd5yyk27mdfw2&d=' + timestamp,
		success: function(httpResponse) {
			response.success(JSON.parse(httpResponse.text));
		},
		error: function(httpResponse) {
			response.error(httpResponse);
		}
	});
});


Parse.Cloud.job("getMovies", function(request, status) {
	var timestamp = new Date().getTime();

	/*
	 * Opening Movies
	 */
	Parse.Cloud.httpRequest({
		url: 'http://api.rottentomatoes.com/api/public/v1.0/lists/movies/opening.json?limit=3&country=us&apikey=qdcwaccyw2tbd5yyk27mdfw2&d=' + timestamp,
		success: function(httpResponse) {
			var response = JSON.parse(httpResponse.text),
				movies = response.movies,
				Movie = Parse.Object.extend('Movie'),
				i = 0,
				numMovies = movies.length,
				count = 0;

			_.each(movies, function(movie, index) {
				/*
				 * check to see if the movie already exists
				 */
				var query = new Parse.Query(Movie);
				query.equalTo('movieId', movie.id);

				query.first().then(function(foundMovie) {
					if (foundMovie) {
						/*
						 * movie exists, update it
						 */
						foundMovie.save({
							movieId: movie.id,
							title: movie.title,
							mpaa_rating: movie.mpaa_rating,
							runtime: movie.runtime,
							synopsis: movie.synopsis,
							ratings: movie.ratings,
							critics_consensus: movie.critics_consensus,
							abridged_cast: movie.abridged_cast,
							posters: movie.posters,
							index: 0
						}, {
							success: function() {
								count += 1;

								if (count === numMovies - 1) {
									status.success('Done! Number of Movies Saved: ' + numMovies);
								}
							}
						});
					} else {
						/*
						 * movie does not exist, create it
						 */
						var newMovie = new Movie();
						newMovie.save({
							movieId: movie.id,
							title: movie.title,
							mpaa_rating: movie.mpaa_rating,
							runtime: movie.runtime,
							synopsis: movie.synopsis,
							ratings: movie.ratings,
							critics_consensus: movie.critics_consensus,
							abridged_cast: movie.abridged_cast,
							posters: movie.posters,
							index: 0
						}, {
							success: function() {
								count += 1;

								if (count === numMovies - 1) {
									status.success('Done! Number of Movies Saved: ' + numMovies);
								}
							}
						});
					}
				}, function() {
					console.log('not found');
				});
			});
		},
		error: function(httpResponse) {
			status.error(httpResponse);
		}
	});

	/*
	 * Now Playing
	 */
	Parse.Cloud.httpRequest({
		url: 'http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=16&country=us&apikey=qdcwaccyw2tbd5yyk27mdfw2&d=' + timestamp,
		success: function(httpResponse) {
			var response = JSON.parse(httpResponse.text),
				movies = response.movies,
				Movie = Parse.Object.extend('Movie'),
				i = 0,
				numMovies = movies.length,
				count = 0;

			_.each(movies, function(movie, index) {
				var query = new Parse.Query(Movie);
				query.equalTo('movieId', movie.id);

				query.first().then(function(foundMovie) {
					if (foundMovie) {
						foundMovie.save({
							movieId: movie.id,
							title: movie.title,
							mpaa_rating: movie.mpaa_rating,
							runtime: movie.runtime,
							synopsis: movie.synopsis,
							ratings: movie.ratings,
							critics_consensus: movie.critics_consensus,
							abridged_cast: movie.abridged_cast,
							posters: movie.posters,
							index: index + 1
						}, {
							success: function() {
								count += 1;

								if (count === numMovies - 1) {
									status.success('Done! Number of Movies Saved: ' + numMovies);
								}
							}
						});
					} else {
						var newMovie = new Movie();
						newMovie.save({
							movieId: movie.id,
							title: movie.title,
							mpaa_rating: movie.mpaa_rating,
							runtime: movie.runtime,
							synopsis: movie.synopsis,
							ratings: movie.ratings,
							critics_consensus: movie.critics_consensus,
							abridged_cast: movie.abridged_cast,
							posters: movie.posters,
							index: index + 1
						}, {
							success: function() {
								count += 1;

								if (count === numMovies - 1) {
									status.success('Done! Number of Movies Saved: ' + numMovies);
								}
							}
						});
					}
				}, function() {
					console.log('not found');
				});
			});
		},
		error: function(httpResponse) {
			status.error(httpResponse);
		}
	});
});