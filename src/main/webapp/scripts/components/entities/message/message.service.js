'use strict';

angular.module('messagyApp')
    .factory('Message', function ($resource, DateUtils) {
        return $resource('api/messages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timePlaced = DateUtils.convertLocaleDateFromServer(data.timePlaced);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.timePlaced = DateUtils.convertLocaleDateToServer(data.timePlaced);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.timePlaced = DateUtils.convertLocaleDateToServer(data.timePlaced);
                    return angular.toJson(data);
                }
            }
        });
    });
