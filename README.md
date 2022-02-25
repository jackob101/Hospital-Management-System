[![CircleCI](https://circleci.com/gh/jackob101/Hospital-Management-System.svg?style=svg)](https://app.circleci.com/pipelines/github/jackob101/Hospital-Management-System?filter=all)

# Hospital Management System

This project aims to help with everyday hospital work like: registering new patients, creating proper patient
documentation, tracking which rooms are taken or need cleaning, taking care of items stock, etc.

# Planned features

## Creating new account

Users that have never been in hospital managed by this project can easily create account. This account will store
information like:

- First name
- Last name
- Some unique identifier ( In Poland PESEL )
- Address

Next with created account they can easily book an appointment.

## Booking appointments

User can view and pick date that fits his/her schedule and add some basic information like what is happening,
appointment type ( Telephonic, walk in etc.) and phone number.

## Patient documentation

Doctors and nurses will have easy access to create full documentation of patient visits to managed hospitals and convert
it into some nice format like PDF.

## Inventory

This module in design is very flexible. Each hospital can have multiple inventories. Each inventory have its own stock
of items, employees can also specify amount of item that should be kept in stock, so they can easily check what is
missing.

## Scans, Operations, Lab etc.

Should take care of storing and allowing for easy access to the results.

## Transfers

This module should take care of creating various documentation that is necessary for transferring patients.

## Room status

Monitors all the patient rooms and notifies if some rooms need cleaning. Also allows to easily check which rooms are
free and ready to take care of patient.

# Details

For more technical and implementation details please look
at [Details](https://github.com/jackob101/Hospital-Management-System/blob/master/details/README.org)