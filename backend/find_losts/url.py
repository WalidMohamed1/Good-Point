from django.urls import path
from . import views
from user_account import views as user_view



urlpatterns = [
    path('lostobject/', views.LostObjectView.as_view(), name='lost_object'),
    path('lostitem/', views.LostItemView.as_view()),
    path('lostperson/', views.LostPersonView.as_view(), name='lost_object'),
    path('lostperson_image/', views.LostPersonImageView.as_view()),
    path('founditem/', views.FoundItemView.as_view()),
    path('foundobject/', views.FoundObjectView.as_view()),
    path('foundperson/', views.FoundPersonView.as_view(), name='lost_object'),
    path('foundperson_image/', views.FoundPersonImageView.as_view(), name='lost_item'),
    path('lostobject/<int:pk>/', views.LostObjectDetailsView.as_view(), name='lost_item'),
    path('lostitem/<int:pk>/', views.LostItemDetailsView.as_view(), name='lost_item'),
    path('lostperson/<int:pk>/', views.LostPersonDetailsView.as_view(), name='lost_item'),
    path('foundobject/<int:pk>/', views.FoundObjectDetalisView.as_view(), name='lost_item'),
    path('founditem/<int:pk>/', views.FoundItemDetailsView.as_view(), name='lost_item'),
    path('lost_person/id=<int:id>/', views.Lost_PersonView.as_view()),
    path('found_person/id=<int:id>/', views.Found_PersonView.as_view()),

    path('LostObjectFilter/', views.LostObjectFilter.as_view()),
    path('LostItemFilter/', views.LostItemFilter.as_view()),
    path('FoundObjectFilter/', views.FoundObjectFilter.as_view()),
    path('FoundItemFilter/', views.FoundItemFilter.as_view()),
    path('founder/<int:id>/', user_view.WhoFoundItemView.as_view(), name='who found item'),
    path('map/', views.MapView.as_view(), name='map'),
]