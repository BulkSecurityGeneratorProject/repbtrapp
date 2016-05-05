'use strict';

angular.module('btravelappApp')
	.controller('CommentsDeleteController', function($scope, $uibModalInstance, entity, Comments) {

        $scope.comments = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Comments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
