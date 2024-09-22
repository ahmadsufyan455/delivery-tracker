## Introduction
<p>This is **Delivery Tracking** app build with **Jetpack Compose**, this app used to track the real time user location every 5 minutes.
This app contains log data in the list view and also show the location with maps marker. Every 10 PM the data will be removed from local database.
This app also continues to track location even though the user is offline.</p>

<p>This project use Clean Architecture and Clean Code to make the project readable, maintainable, and testable</p>

## Features
* Location tracking
* Log location list
* Location maps marker
* Notification for tracking location and remove data
* Online and Offline tracking

## Library
* Paging
* Room
* Work Manager
* Maps
* Koin
* Data Store
* Compose Navigation

This project also contains viewmodel unit test with scenario:
* Ensure the emits log location data is correct ✅

## Installation
Clone or download this repository into your computer and open with latest Android Studio.
```
https://github.com/ahmadsufyan455/delivery-tracker.git
```

## Screenshot

<table>
    <tr>
        <td><img src="https://github.com/ahmadsufyan455/delivery-tracker/blob/main/screenshot/location-permission.jpg" alt="delivery tracking" border="0" /></td>
        <td><img src="https://github.com/ahmadsufyan455/delivery-tracker/blob/main/screenshot/notif-permission.jpg" alt="delivery tracking" border="0" /></td>
        <td><img src="https://github.com/ahmadsufyan455/delivery-tracker/blob/main/screenshot/log-data.jpg" alt="delivery tracking" border="0" /></td>
    </tr>
    <tr>
        <td><img src="https://github.com/ahmadsufyan455/delivery-tracker/blob/main/screenshot/marker-data.jpg" alt="delivery tracking" border="0" /></td>
        <td><img src="https://github.com/ahmadsufyan455/delivery-tracker/blob/main/screenshot/remove-notif.png" alt="delivery tracking" border="0" /></td>
    </tr>
</table>


