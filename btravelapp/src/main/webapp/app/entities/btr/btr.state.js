(function() {
    'use strict';

    angular
        .module('btravelappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('btr', {
            parent: 'entity',
            url: '/btr?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'btravelappApp.btr.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/btr/btrs.html',
                    controller: 'BtrController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('btr');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('btr-detail', {
            parent: 'entity',
            url: '/btr/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'btravelappApp.btr.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/btr/btr-detail.html',
                    controller: 'BtrDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('btr');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Btr', function($stateParams, Btr) {
                    return Btr.get({id : $stateParams.id});
                }]
            }
        })
        .state('btr.new', {
            parent: 'btr',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/btr/btr-dialog.html',
                    controller: 'BtrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                start_date: null,
                                end_date: null,
                                location: null,
                                center_cost: null,
                                request_date: null,
                                last_modified_date: null,
                                suma_totala: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('btr', null, { reload: true });
                }, function() {
                    $state.go('btr');
                });
            }]
        })
        .state('btr.edit', {
            parent: 'btr',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/btr/btr-dialog.html',
                    controller: 'BtrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Btr', function(Btr) {
                            return Btr.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('btr', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('btr.delete', {
            parent: 'btr',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/btr/btr-delete-dialog.html',
                    controller: 'BtrDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Btr', function(Btr) {
                            return Btr.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('btr', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
