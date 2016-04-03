(function() {
    'use strict';

    angular
        .module('iaConnectorServerApp')
        .factory('FillerService', FillerService);

    FillerService.$inject = ['$filter', '$http'];

    function FillerService ($filter, $http) {
        return {
            getGames: function(callback) {
                 return $http.get('iaconnector/games').then(callback);
            },
            getGame: function(idGame) {
                 return $http.get('iaconnector/game?idGame=' + idGame);
            },
            startGame: function(idGame) {
                 return $http.get('iaconnector/startGame?idGame=' + idGame);
            },
            removeGame: function(idGame) {
                 $http.get('iaconnector/removeGame?idGame=' + idGame);
             },
             addGame : function(gridSize) {
                return $http.get('iaconnector/addGame?gridSize=' + gridSize);
             }
        };
    }
})();
