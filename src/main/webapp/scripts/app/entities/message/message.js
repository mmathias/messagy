'use strict';

angular.module('messagyApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('message', {
                parent: 'entity',
                url: '/message',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Messages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/message/messages.html',
                        controller: 'MessageController'
                    }
                },
                resolve: {
                }
            })
            .state('messageDetail', {
                parent: 'entity',
                url: '/message/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Message'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/message/message-detail.html',
                        controller: 'MessageDetailController'
                    }
                },
                resolve: {
                }
            });
    });
