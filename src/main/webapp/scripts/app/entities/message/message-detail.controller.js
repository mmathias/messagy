'use strict';

angular.module('messagyApp')
    .controller('MessageDetailController', function ($scope, $stateParams, Message, User) {
        $scope.message = {};
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
              $scope.message = result;
            });
        };
        $scope.load($stateParams.id);
    });
