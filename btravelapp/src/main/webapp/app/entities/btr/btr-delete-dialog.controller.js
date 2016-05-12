(function() {
    'use strict';

    angular
        .module('btravelappApp')
        .controller('BtrDeleteController',BtrDeleteController);

    BtrDeleteController.$inject = ['$uibModalInstance', 'entity', 'Btr'];

    function BtrDeleteController($uibModalInstance, entity, Btr) {
        var vm = this;
        vm.btr = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Btr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
