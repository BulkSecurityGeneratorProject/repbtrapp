'use strict';

describe('Controller Tests', function() {

    describe('Comments Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockComments, MockBtr;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockComments = jasmine.createSpy('MockComments');
            MockBtr = jasmine.createSpy('MockBtr');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Comments': MockComments,
                'Btr': MockBtr
            };
            createController = function() {
                $injector.get('$controller')("CommentsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'btravelappApp:commentsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
