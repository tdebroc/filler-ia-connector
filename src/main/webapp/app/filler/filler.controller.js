(function() {
    'use strict';

    angular
        .module('iaConnectorServerApp')
        .controller('FillerController', FillerController);

    FillerController.$inject = ['$scope', 'Principal', 'LoginService', 'FillerService', '$mdDialog',
            '$timeout', '$interval'];

    function FillerController ($scope, Principal, LoginService, FillerService, $mdDialog, $timeout, $interval) {

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

        $scope.startGame = function(idGame) {
            FillerService.startGame(idGame);
        }

        $scope.refreshGame = function(idGame) {
            FillerService.getGame(idGame).then(function(response) {
                $scope.games[idGame] = response.data;
                $scope.selectGame(idGame)
            })
         //   gameRefresher = $interval($scope.refreshGame.bind(this, idGame), 1000);
        }


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
