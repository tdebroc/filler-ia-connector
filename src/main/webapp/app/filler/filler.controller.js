(function() {
    'use strict';

    angular
        .module('iaConnectorServerApp')
        .controller('FillerController', FillerController);

    FillerController.$inject = ['$scope', 'Principal', 'LoginService', 'FillerService', '$mdDialog',
            '$timeout', '$interval', 'FillerSocketService', '$stateParams', '$sce'];

    function FillerController ($scope, Principal, LoginService, FillerService, $mdDialog, $timeout,
                                $interval, FillerSocketService, $stateParams, $sce) {

        //=====================================================================
        // Init
        //=====================================================================
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
        // Sockets.
        //=====================================================================
        FillerSocketService.connect();
        FillerSocketService.receive().then(null, null, function(game) {
            if (game.idGame == $scope.currentIdGame) {
                $scope.currentGame = game;
                $scope.selectGame($scope.currentIdGame)
            }
        });
        FillerSocketService.subscribe();
        FillerSocketService.subscribeRefreshAllGames();

        FillerSocketService.receiveAllGames().then(null, null, function(allGames) {
            $scope.games = allGames;
        })

        //=====================================================================
        // Display/refresh/select Game
        //=====================================================================
        $scope.refreshGames = function() {
            $scope.games = FillerService.getGames(function(response) {
                $scope.games = response.data;
                $scope.selectGame($stateParams.gameId)
            });
        }
        $scope.refreshGames();


        var gameRefresher;
        $scope.selectGame = function(idGame) {
            if (!idGame || idGame == 1) {
                $scope.currentGame = gameMainPage;
                $scope.currentIdGame = 1;
                return;
            }
            var keys = Object.keys($scope.games);
            var idGameLast = $scope.games[keys[keys.length - 1]].idGame
            $scope.currentIdGame = idGame && idGame > 0 ? idGame : idGameLast;
            $scope.refreshGame($scope.currentIdGame);
            //gameRefresher = $interval( $scope.refreshGame.bind(this, idGame), 1000);
        }


        $scope.refreshGame = function(idGame) {
            FillerService.getGame(idGame).then(function(response) {
                $scope.currentGame = response.data;
                displayGame(response.data);
            })
        }

        function displayGame(game) {
            var grid = game.grid.grid;
            var gridEl = $("<table  id='filler-table-grid'></table>");
            for (var i = 0; i < grid.length; i++) {
                var lineEl = $("<tr></tr>");
                for (var j = 0; j < grid[i].length; j++) {
                    var cell = grid[i][j];
                    var cellEl = $("<td></td>");
                    cellEl.attr("style", "background-color: " +  $scope.getColor(cell.color));
                    cellEl.html($scope.displayPlayer(i, j, game, cell.color));
                    lineEl.append(cellEl);
                }
                gridEl.append(lineEl);
            }
            $("#filler-grid-wrapper").html("");
            $("#filler-grid-wrapper").append(gridEl);
        }

        // Start
        if (!$scope.currentGame) {
            //$scope.selectGame($stateParams.gameId);
        }

        $scope.displayPlayer = function(x, y, currentGame, color) {
            for (var i = 0;i < currentGame.players.length; i++) {
                var pos = currentGame.players[i].initPosition;
                if (pos.x == x && pos.y == y) {
                    return i + 1;
                }
            }
            return $scope.displayCharColor ? color : ("&nbsp;&nbsp;&nbsp;");
        }

        $scope.availableColor = function(currentGame) {
            var colors = {};
            for (var c in  $scope.mapColor) {
                colors[c] = $scope.mapColor[c];

            }
            for (var i = 0; i < currentGame.players.length; i++) {
                delete colors[currentGame.players[i].playerColor];
            }

            return colors;
        }

        FillerService.getContent().then(function(allContent) {
            $scope.rulesText = allContent.data[0].contentText;
            $scope.IACompetitionText = allContent.data[1].contentText;
            $scope.bonusText = allContent.data[2].contentText;
        })
        //=====================================================================
        // Play Game
        //=====================================================================
        $scope.startGame = function(idGame) {
            FillerService.startGame(idGame);
        }

        $scope.addPlayer = function(idGame) {
            var idPlayerTurn = $scope.currentGame.players.length;
            var newPlayerName = $("#newPlayerName").val()
            FillerService.addPlayer(idGame, newPlayerName).then(function(response) {
                var playerInstance = JSON.parse(response.data)
                var idPlayerTurn = playerInstance.idPlayer;
                var key = getKey(idGame, idPlayerTurn);
                $scope.currentPlayers[key] = playerInstance.UUID || playerInstance.uuid;
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
            return game.started && game.currentIdPlayerTurn == idPlayerTurn && !game.finished;
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
                FillerService.addGame($scope.addGameGridSize).then(function(response) {
                    $scope.selectGame(response.data);
                    $scope.refreshGames();
                    // $scope.$digest();

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
                $scope.selectGame();
            }, function() {
              $scope.status = 'You decided to keep your debt.';
            });
          };



        //=====================================================================
        // Fix width in smartphone
        //=====================================================================
        function refreshHeight() {
            setTimeout(function() {
                var height = $(document).height() - 87;
                $("#filler-list").height(height - $("#filler-list").offset().top);
            }, 100)
        }
        $(window).resize(refreshHeight);
        $(document).ready(refreshHeight)

        //=====================================================================
        // Fix width in smartphone
        //=====================================================================
        $scope.toArray = function(obj) {
            var arr = []
            for (var key in obj) {
                arr.push(obj[key]);
            }
            return arr;
        }
        //=====================================================================
        // FILLER
        //=====================================================================

        var FILLER = "JOOOOJOOOROOOROOBOOOOOOOJ " +
                     "OVVVOVOVOOOVOOOVVVOVVVVRO " +
                     "OVOJOVOVOBOVOOOVOOOVOOOVO " +
                     "OVVOOVOVOOOVOOOVVOOVVVVOO " +
                     "OVOOOVOVOOOVJJOVOOOVOVOOO " +
                     "OVOJOVOVVVOVVVOVVVOVOOVOO " +
                     "JOOBOOOOOOOOOOOOBOOOOOOJO ";
        var gameMainPage = {};
        gameMainPage.grid = { grid : []};
        gameMainPage.players = []
        gameMainPage.started = true;
        gameMainPage.finished = true;
        gameMainPage.idGame = 1
        var lines = FILLER.split(" ");
        for (var i = 0; i < lines.length; i++) {
            var line = [];
            for (var j = 0; j < lines[i].length; j++) {
                line.push({color : lines[i].charAt(j)})
            }
            gameMainPage.grid.grid.push(line);
        }
        displayGame(gameMainPage);
    }
})();
