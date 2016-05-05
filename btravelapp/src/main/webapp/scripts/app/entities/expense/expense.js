'use strict';

angular.module('btravelappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expense', {
                parent: 'entity',
                url: '/expenses',
                data: {
                    authorities: ['ROLE_USER'],
                	//authorities: ['ROLE_SUPPLIER'],
                	//authorities: ['ROLE_MANAGER'],
                    pageTitle: 'btravelappApp.expense.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expense/expenses.html',
                        controller: 'ExpenseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expense');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expense.detail', {
                parent: 'entity',
                url: '/expense/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                	//authorities: ['ROLE_SUPPLIER'],
                    pageTitle: 'btravelappApp.expense.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expense/expense-detail.html',
                        controller: 'ExpenseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expense');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Expense', function($stateParams, Expense) {
                        return Expense.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expense.new', {
                parent: 'expense',
                url: '/new',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_SUPPLIER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense/expense-dialog.html',
                        controller: 'ExpenseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    expense_cost: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expense', null, { reload: true });
                    }, function() {
                        $state.go('expense');
                    })
                }]
            })
            .state('expense.edit', {
                parent: 'expense',
                url: '/{id}/edit',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_SUPPLIER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense/expense-dialog.html',
                        controller: 'ExpenseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Expense', function(Expense) {
                                return Expense.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expense', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('expense.delete', {
                parent: 'expense',
                url: '/{id}/delete',
                data: {
                    //authorities: ['ROLE_USER'],
                	authorities: ['ROLE_SUPPLIER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expense/expense-delete-dialog.html',
                        controller: 'ExpenseDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Expense', function(Expense) {
                                return Expense.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expense', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
