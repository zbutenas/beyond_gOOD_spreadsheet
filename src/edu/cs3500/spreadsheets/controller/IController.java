package edu.cs3500.spreadsheets.controller;

/**
 * Interface represents a controller for a worksheet. Holds one method that
 * opens a new spreadsheet GUI.
 */
public interface IController {

  /**
   * This method will open up a new editable spreadsheet application based on the given model and
   * other key parameters of the view.
   */
  void open();
}
