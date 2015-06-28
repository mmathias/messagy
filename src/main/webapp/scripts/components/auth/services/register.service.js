'use strict';

angular.module('messagyApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


