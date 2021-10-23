from django.db import models
from django.contrib.auth.models import AbstractUser
from rest_framework_simplejwt.tokens import RefreshToken


# Create your models here.

def profile_pic_dir(instance, filename):
    return f"profile/{filename}"


def idcard_pic_dir(instance, filename):
    return f"idcard/{filename}"


class User(AbstractUser):
    email = models.EmailField(max_length=1, blank=True)
    phone = models.CharField(max_length=20, unique=True)
    birthdate = models.DateField()
    city = models.CharField(max_length=35)
    profile_pic = models.ImageField(blank=True, null=True, upload_to=profile_pic_dir)
    id_card_pic = models.ImageField(blank=True, null=True, upload_to=idcard_pic_dir)

    def __str__(self):
        return self.username

    def tokens(self):
        refresh = RefreshToken.for_user(self)
        return {
            'refresh': str(refresh),
            'access': str(refresh.access_token)
        }

    class Meta:
        db_table = 'user'
