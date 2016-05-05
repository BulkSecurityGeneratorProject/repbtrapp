'use strict';

angular.module('btravelappApp')
	.controller('BtrDeleteController', function($scope, $uibModalInstance, entity, Btr) {

        $scope.btr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Btr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
