'use strict';

describe('Controller Tests', function() {

    describe('Expense Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExpense, MockBtr, MockExpense_type;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.query('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExpense = jasmine.createSpy('MockExpense');
            MockBtr = jasmine.createSpy('MockBtr');
            MockExpense_type = jasmine.createSpy('MockExpense_type');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Expense': MockExpense,
                'Btr': MockBtr,
                'Expense_type': MockExpense_type
            };
            createController = function() {
                $injector.query('$controller')("ExpenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'btravelappApp:expenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
