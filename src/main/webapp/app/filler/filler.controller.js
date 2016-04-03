(function() {
    'use strict';

    angular
        .module('iaConnectorServerApp')
        .controller('FillerController', FillerController);

    FillerController.$inject = ['$scope', 'Principal', 'LoginService', 'FillerService', '$mdDialog',
            '$timeout', '$interval'];

    function FillerController ($scope, Principal, LoginService, FillerService, $mdDialog, $timeout, $interval) {

        $scope.currentPlayers = {};
        function loadCurrentPlayers() {
            var currentPlayerString = localStorage.getItem("currentPlayers");
            if (!currentPlayerString) {
                $scope.currentPlayers = {};
            } else {
                $scope.currentPlayers = JSON.parse(currentPlayerString);
            }
        }
        loadCurrentPlayers();

        $scope.mapColor = {
            'R' : "red",
            'J' : "yellow",
            'B' : "blue",
            'V' : "green",
            'O' : "orange",
            'I' : "indigo"
        }
        $scope.getColor = function(color) {
            return $scope.mapColor[color];
        }

        //=====================================================================
        // Display/refresh/select Game
        //=====================================================================
        $scope.refreshGames = function() {
            $scope.games = FillerService.getGames(function(response) {
                $scope.games = response.data;
                if (!$scope.currentGame) {
                    $scope.selectGame(1);
                }
            });
            // setTimeout(refreshGames, 1000)
        }
        $scope.refreshGames();

        var gameRefresher;
        $scope.selectGame = function(idGame) {
            $scope.currentIdGame = idGame;
            $scope.currentGame = $scope.games[idGame];

            if (gameRefresher) {
                // debugger;
                $interval.cancel(gameRefresher);
            }
            gameRefresher = $interval( $scope.refreshGame.bind(this, idGame), 1000);
        }


        $scope.refreshGame = function(idGame) {
            FillerService.getGame(idGame).then(function(response) {
                $scope.games[idGame] = response.data;
                $scope.selectGame(idGame)
            })
        }

        //=====================================================================
        // Play Game
        //=====================================================================
        $scope.startGame = function(idGame) {
            FillerService.startGame(idGame);
        }

        $scope.addPlayer = function(idGame) {
            var idPlayerTurn = $scope.currentGame.players.length;
            FillerService.addPlayer(idGame).then(function(response) {
                var key = getKey(idGame, idPlayerTurn);
                $scope.currentPlayers[key] = response.data;
                localStorage.setItem("currentPlayers", JSON.stringify($scope.currentPlayers));
            });
        }

        function getKey(idGame, idPlayerTurn) {
            return idGame + "#" + idPlayerTurn
        }

        $scope.sendMove = function(color, currentIdGame, idPlayerTurn) {
            var playerUUID = $scope.currentPlayers[getKey(currentIdGame, idPlayerTurn)];
            FillerService.sendMove(color, playerUUID).then(function() {
                $scope.refreshGame(currentIdGame);
            });
        }

        $scope.isCurrentPlayer = function(currentIdGame, idPlayerTurn) {
            return $scope.currentPlayers[getKey(currentIdGame, idPlayerTurn)]
        }

        $scope.isPlayerTurn = function(game, idPlayerTurn) {
            return game.started && game.currentIdPlayerTurn == idPlayerTurn;
        }


        $scope.countPoints = function(game, color) {
            var grid = game.grid.grid;
            var points = 0;
            for (var i = 0; i < grid.length; i++) {
                for (var j = 0; j < grid.length; j++) {
                    if (grid[i][j].color == color) points++;
                }
            }
            return points;
        }

        //=====================================================================
        // Add Game
        //=====================================================================
        $scope.addGameGridSize = 13;
        $scope.addGameModal = function(ev) {
            $mdDialog.show({
              controller: DialogController,
              templateUrl: '/app/filler/addGameDialog.tmpl.html',
              parent: angular.element(document.body),
              targetEvent: ev,
              preserveScope :true,
              scope : $scope,
              clickOutsideToClose:true
            })
            .then(function(answer) {
              $scope.status = 'You said the information was "' + answer + '".';
            }, function() {
              $scope.status = 'You cancelled the dialog.';
            });

            function DialogController($scope, $mdDialog) {
              $scope.cancel = function() {
                $mdDialog.cancel();
              };
              $scope.addGame = function() {
                FillerService.addGame($scope.addGameGridSize).then(function() {
                    $scope.refreshGames();
                });
                $mdDialog.hide();
              };
            }

        }

        //=====================================================================
        // Delete Game
        //=====================================================================
        $scope.showConfirmDelete = function(ev) {
            var confirm = $mdDialog.confirm()
                  .title('Are you sure you want to remove this Game ?')
                  .textContent('This game will be definitely erased.')
                  .ariaLabel('Delete')
                  .targetEvent(ev)
                  .ok('Remove')
                  .cancel('No');
            $mdDialog.show(confirm).then(function() {
                delete $scope.games[$scope.currentIdGame];
                FillerService.removeGame($scope.currentIdGame);
                $scope.selectGame(1);
            }, function() {
              $scope.status = 'You decided to keep your debt.';
            });
          };


    }
})();
