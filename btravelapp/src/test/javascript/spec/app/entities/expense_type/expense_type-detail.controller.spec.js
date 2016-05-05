'use strict';

describe('Controller Tests', function() {

    describe('Expense_type Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExpense_type, MockExpense;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExpense_type = jasmine.createSpy('MockExpense_type');
            MockExpense = jasmine.createSpy('MockExpense');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Expense_type': MockExpense_type,
                'Expense': MockExpense
            };
            createController = function() {
                $injector.get('$controller')("Expense_typeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'btravelappApp:expense_typeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
