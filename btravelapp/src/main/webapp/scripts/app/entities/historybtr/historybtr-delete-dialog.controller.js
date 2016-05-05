'use strict';

angular.module('btravelappApp')
	.controller('HistorybtrDeleteController', function($scope, $uibModalInstance, entity, Historybtr) {

        $scope.historybtr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Historybtr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
