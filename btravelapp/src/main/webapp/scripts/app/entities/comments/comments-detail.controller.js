'use strict';

angular.module('btravelappApp')
    .controller('CommentsDetailController', function ($scope, $rootScope, $stateParams, entity, Comments, Btr) {
        $scope.comments = entity;
        $scope.load = function (id) {
            Comments.get({id: id}, function(result) {
                $scope.comments = result;
            });
        };
        var unsubscribe = $rootScope.$on('btravelappApp:commentsUpdate', function(event, result) {
            $scope.comments = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
