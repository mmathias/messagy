'use strict';

angular.module('messagyApp')
    .controller('MessageController', function ($scope, Message, User, ParseLinks) {
        $scope.messages = [];
        $scope.users = User.query();
        $scope.page = 1;

        $scope.initiator = false;

        $scope.socket = {
            client: null,
            stomp: null
        };

        $scope.loadAll = function() {
            Message.query({page: $scope.page, per_page: 20}, function(result, headers) {
                if (result.length) {
                    for (var i = 0; i < result.length; i++) {
                        $scope.messages.push(result[i]);
                    }
                }
            });
        };

        $scope.reset = function() {
            $scope.page = 1;
            $scope.messages = [];
            $scope.loadAll();
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
                $('#saveMessageModal').modal('show');
            });
        };

        $scope.save = function () {
            $scope.initiator = true;
            if ($scope.message.id != null) {
                Message.update($scope.message,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Message.save($scope.message,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
                $('#deleteMessageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            $scope.initiator = true;
            Message.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteMessageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Message.query({query: $scope.searchQuery}, function(result) {
                $scope.messages = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveMessageModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.message = {currencyFrom: null, currencyTo: null, amountSell: null, amountBuy: null, rate: null, timePlaced: null, originatingCountry: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };

        // Real time messaging
        $scope.notify = function(message) {
            // if false it means that it was changed somewhere else,
            // therefore messages need to be updated here also
            if (!$scope.initiator) {
                Message.query(function(messages) {
                    $scope.messages = messages;
                });
            }
            $scope.initiator = false;
            $scope.buildChart(JSON.parse(message.body));
        };

        $scope.reconnect = function() {
            setTimeout($scope.connect, 10000);
        };

        $scope.connect = function() {
            $scope.socket.client = new SockJS('/websocket/tracker');
            $scope.socket.stomp = Stomp.over($scope.socket.client);
            $scope.socket.stomp.connect({}, function() {
                $scope.socket.stomp.subscribe("/topic/tracker", $scope.notify);
            });
            $scope.socket.client.onclose = $scope.reconnect;
        };

        $scope.connect();


        $scope.buildChart = function(dataChart) {
            if (!dataChart.headers) return;

            $scope.chart = {};
            $scope.chart.type = "LineChart";
            $scope.chart.options = {
                hAxis: {
                    title: 'Day'
                },
                vAxis: {
                    title: 'Amount sold'
                },
                displayExactValues: true,
                width: 700,
                height: 500
            };

            $scope.chart.formatters = {
                number : [{
                    columnNum: 1,
                    pattern: "$ #,##0.00"
                }]
            };

            $scope.chart.data = {
                cols: [],
                rows: []
            };

            for (var i = 0; i < dataChart.headers.length; i++) {
                var header = dataChart.headers[i];
                $scope.chart.data.cols.push({ id: header.id, label: header.label, type: header.type});
            }

            for (var i = 0; i < dataChart.data.length; i++) {
                var row = dataChart.data[i],
                    formattedRow = {
                        c: []
                    };

                for (var j = 0; j < row.length; j++) {
                    formattedRow.c.push({v : row[j]});
                }

                $scope.chart.data.rows.push(formattedRow);
            }

            $scope.chart.data = JSON.parse(JSON.stringify($scope.chart.data));
        };


    });
