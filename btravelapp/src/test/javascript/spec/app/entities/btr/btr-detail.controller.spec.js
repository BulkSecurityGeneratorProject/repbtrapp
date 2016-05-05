'use strict';

describe('Controller Tests', function() {

    describe('Btr Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBtr, MockUser, MockExpense;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBtr = jasmine.createSpy('MockBtr');
            MockUser = jasmine.createSpy('MockUser');
            MockExpense = jasmine.createSpy('MockExpense');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Btr': MockBtr,
                'User': MockUser,
                'Expense': MockExpense
            };
            createController = function() {
                $injector.get('$controller')("BtrDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'btravelappApp:btrUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
