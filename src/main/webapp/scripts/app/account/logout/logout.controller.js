'use strict';

angular.module('messagyApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
