from django.db import models
from user_account.models import User
from notification.models import Notification


# Create your models here.

class LostObject(models.Model):
    date = models.DateField()
    city = models.CharField(max_length=35)
    user_id = models.ForeignKey(User, related_name='lost', on_delete=models.CASCADE, db_column='user_id')
    is_matched = models.BooleanField(default=False)

    class Meta:
        db_table = 'lost_object'


def lost_person_images(instance,filename):
    return f"lostperson/{instance.pk}.jpg"


class LostPersonImage(models.Model):
    id_lp = models.IntegerField
    image_number = models.IntegerField
    image = models.ImageField(blank=True, null=True, upload_to=lost_person_images)

    class Meta:
        db_table = 'lost_person_image'


class LostPerson(models.Model):
    id = models.OneToOneField(LostObject, primary_key=True, on_delete=models.CASCADE, db_column='id', blank=True)
    name = models.CharField(max_length=150)
    #person_image = models.ManyToManyField(LostPersonImage, db_column='images', related_name='images')
    image = models.ImageField(blank=True, null=True, upload_to=lost_person_images)

    class Meta:
        db_table = 'lost_person'


class LostItem(models.Model):
    id = models.OneToOneField(LostObject, primary_key=True, on_delete=models.CASCADE, db_column='id')
    type = models.CharField(max_length=20)
    color = models.CharField(max_length=20)
    brand = models.CharField(max_length=50)
    description = models.CharField(max_length=700)
    serial_number = models.CharField(max_length=100, blank=True, null=True)
    image = models.ImageField(blank=True, null=True)

    class Meta:
        db_table = 'lost_item'


class FoundObject(models.Model):
    date = models.DateField()
    longitude = models.DecimalField(max_digits=14, decimal_places=10, default=0.0)
    latitude = models.DecimalField(max_digits=14, decimal_places=10, default=0.0)
    city = models.CharField(max_length=35)
    user_id = models.ForeignKey(User, related_name='found', on_delete=models.CASCADE, db_column='user_id')
    is_matched = models.BooleanField(default=False)

    class Meta:
        db_table = 'found_object'


def found_person_images(instance,filename):
    return f"foundperson/{instance.pk}.jpg"


class FoundPerson(models.Model):
    id = models.OneToOneField(FoundObject, primary_key=True, on_delete=models.CASCADE, db_column='id')
    name = models.CharField(max_length=150, blank=True, null=True)
    image = models.ImageField(blank=True, null=True, upload_to=found_person_images)

    class Meta:
        db_table = 'found_person'


class FoundPersonImage(models.Model):
    id = models.OneToOneField(FoundPerson, primary_key=True, on_delete=models.CASCADE, db_column='id')
    image = models.ImageField(unique=True)

    class Meta:
        db_table = 'found_person_image'
        unique_together = (('id', 'image'),)


class FoundItem(models.Model):
    id = models.OneToOneField(FoundObject, primary_key=True, on_delete=models.CASCADE, db_column='id')
    type = models.CharField(max_length=20)
    color = models.CharField(max_length=20)
    brand = models.CharField(max_length=50)
    description = models.CharField(max_length=700)
    serial_number = models.CharField(max_length=100, blank=True, null=True)
    image = models.ImageField(null=True, blank=True)

    class Meta:
        db_table = 'found_item'


class Candidate(models.Model):
    id_fi = models.ForeignKey(FoundItem, related_name='candidate', on_delete=models.CASCADE, db_column='found_item_id')
    id_li = models.ForeignKey(LostItem, related_name='candidate', on_delete=models.CASCADE, db_column='lost_item_id')
    percent = models.DecimalField(max_digits=5, decimal_places=4)
    is_matched = models.BooleanField(default=False)
    notify_id = models.ForeignKey(Notification, related_name='reach_candidates_to_who_found', on_delete=models.CASCADE,
                                  db_column='notify_id')

    class Meta:
        db_table = 'candidate'
        unique_together = (('id_li', 'id_fi'),)


class MatchedPerson(models.Model):
    id_fp = models.OneToOneField(FoundObject, primary_key=True, unique=True, related_name='match',
                                 on_delete=models.CASCADE, db_column='found_person_id')
    id_lp = models.OneToOneField(LostObject, unique=True, related_name='match', on_delete=models.CASCADE,
                                 db_column='lost_person_id')
    date_of_receiving = models.DateTimeField(auto_now_add=True)
    percent = models.DecimalField(max_digits=7, decimal_places=4)
    notify_id_fp = models.ForeignKey(Notification, related_name='reach_match_to_who_found', on_delete=models.CASCADE,
                                     db_column='notify_id_fp')
    notify_id_lp = models.ForeignKey(Notification, related_name='reach_match_to_who_lost', on_delete=models.CASCADE,
                                     db_column='notify_id_lp')

    class Meta:
        db_table = 'matched_Person'
