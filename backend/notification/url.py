from django.urls import path
from . import views

urlpatterns = [
    path('', views.NotificationView.as_view(), name='notification'),
    path('user_id=<int:user_id>/', views.UserNotificationView.as_view(), name='notification'),
    path('read/<int:id>/', views.UpdateReadNotification.as_view(), name='update read notification'),
    path('sent/<int:id>/', views.UpdateSentNotification.as_view(), name='update sent notification'),
    path('new/<int:user_id>/', views.IsThereNewNotification.as_view(), name='is there new notification'),
]
