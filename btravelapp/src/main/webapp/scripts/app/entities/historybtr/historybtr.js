'use strict';

angular.module('btravelappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('historybtr', {
                parent: 'entity',
                url: '/historybtrs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'btravelappApp.historybtr.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/historybtr/historybtrs.html',
                        controller: 'HistorybtrController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('historybtr');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('historybtr.detail', {
                parent: 'entity',
                url: '/historybtr/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'btravelappApp.historybtr.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/historybtr/historybtr-detail.html',
                        controller: 'HistorybtrDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('historybtr');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Historybtr', function($stateParams, Historybtr) {
                        return Historybtr.get({id : $stateParams.id});
                    }]
                }
            })
            .state('historybtr.new', {
                parent: 'historybtr',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/historybtr/historybtr-dialog.html',
                        controller: 'HistorybtrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    btrstatusbefore: null,
                                    btrstatusafter: null,
                                    change_date: null,
                                    changed_by: null,
                                    start_date: null,
                                    end_date: null,
                                    assigned_to: null,
                                    assigned_from: null,
                                    location: null,
                                    center_cost: null,
                                    request_date: null,
                                    last_modified_date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('historybtr', null, { reload: true });
                    }, function() {
                        $state.go('historybtr');
                    })
                }]
            })
            .state('historybtr.edit', {
                parent: 'historybtr',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/historybtr/historybtr-dialog.html',
                        controller: 'HistorybtrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Historybtr', function(Historybtr) {
                                return Historybtr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('historybtr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('historybtr.delete', {
                parent: 'historybtr',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/historybtr/historybtr-delete-dialog.html',
                        controller: 'HistorybtrDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Historybtr', function(Historybtr) {
                                return Historybtr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('historybtr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
