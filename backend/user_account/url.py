from django.urls import path
from . import views

urlpatterns = [
    path('signup/', views.SignupView.as_view(), name='signup'),
    path('signin/',views.LoginView.as_view(), name='signin'),
    path('signout/',views.LogoutView.as_view(), name='signout'),
    path('setidcard/',views.SetIdCard.as_view(), name='setIdCard'),
]