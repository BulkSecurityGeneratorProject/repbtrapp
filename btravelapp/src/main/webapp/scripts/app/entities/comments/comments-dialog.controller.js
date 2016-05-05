'use strict';

angular.module('btravelappApp').controller('CommentsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Comments', 'Btr',
        function($scope, $stateParams, $uibModalInstance, entity, Comments, Btr) {

        $scope.comments = entity;
        $scope.btrs = Btr.query();
        $scope.load = function(id) {
            Comments.get({id : id}, function(result) {
                $scope.comments = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:commentsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.comments.id != null) {
                Comments.update($scope.comments, onSaveSuccess, onSaveError);
            } else {
                Comments.save($scope.comments, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
