'use strict';

angular.module('btravelappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expense_type', {
                parent: 'entity',
                url: '/expense_types',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_SUPPLIER'],
                    pageTitle: 'btravelappApp.expense_type.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expense_type/expense_types.html',
                        controller: 'Expense_typeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expense_type');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expense_type.detail', {
                parent: 'entity',
                url: '/expense_type/{id}',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_SUPPLIER'],
                    pageTitle: 'btravelappApp.expense_type.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expense_type/expense_type-detail.html',
                        controller: 'Expense_typeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expense_type');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Expense_type', function($stateParams, Expense_type) {
                        return Expense_type.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expense_type.new', {
                parent: 'expense_type',
                url: '/new',
                data: {
                   // authorities: ['ROLE_USER'],
                	authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense_type/expense_type-dialog.html',
                        controller: 'Expense_typeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expense_type', null, { reload: true });
                    }, function() {
                        $state.go('expense_type');
                    })
                }]
            })
            .state('expense_type.edit', {
                parent: 'expense_type',
                url: '/{id}/edit',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense_type/expense_type-dialog.html',
                        controller: 'Expense_typeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Expense_type', function(Expense_type) {
                                return Expense_type.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expense_type', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('expense_type.delete', {
                parent: 'expense_type',
                url: '/{id}/delete',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense_type/expense_type-delete-dialog.html',
                        controller: 'Expense_typeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Expense_type', function(Expense_type) {
                                return Expense_type.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expense_type', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
