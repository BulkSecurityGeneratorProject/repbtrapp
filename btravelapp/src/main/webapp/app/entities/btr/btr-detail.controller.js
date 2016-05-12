(function() {
    'use strict';

    angular
        .module('btravelappApp')
        .controller('BtrDetailController', BtrDetailController);

    BtrDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Btr', 'User', 'Expense'];

    function BtrDetailController($scope, $rootScope, $stateParams, entity, Btr, User, Expense) {
        var vm = this;
        vm.btr = entity;
        
        var unsubscribe = $rootScope.$on('btravelappApp:btrUpdate', function(event, result) {
            vm.btr = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
