from django.db import models
from user_account.models import User


# Create your models here.

class Notification(models.Model):
    title = models.CharField(max_length=30)
    description = models.CharField(max_length=100)
    type = models.IntegerField()
    date_time = models.DateTimeField(auto_now_add=True)
    is_sent = models.BooleanField(default=False)
    is_read = models.BooleanField(default=False)
    user_id = models.ForeignKey(User, related_name='notify', on_delete=models.CASCADE, db_column='user_id')

    class Meta:
        db_table = 'notification'
