#
# This is a Shiny web application. You can run the application by clicking
# the 'Run App' button above.
#
# Find out more about building applications with Shiny here:
#
#    http://shiny.rstudio.com/
#

library(shiny)
library(tidyverse)
library(DT)
library(plotly)
library(leaflet)
library(ggmap)

fire_df  <- read_csv("../data/H_FIRE_PNT.csv")

# Define UI for application that draws a histogram
ui <- fluidPage(

    # Application title
    titlePanel("Old Faithful Geyser Data"),

    # Sidebar with a slider input for number of bins 
    #Input: select years
    sliderInput("year", 
                label ="Year",
                min = 1990, max = 2015,
                value = 1975,
                step = 1, sep = ""),

        # Show a plot of the generated distribution
        mainPanel(
          tabsetPanel(type = "tabs",
                  tabPanel("Plot",
                           leafletOutput("map"))
        )))

# Define server logic required to draw a histogram
server <- function(input, output) {

  output$map <- renderLeaflet({
    leaflet() %>% setView(lng = -120, lat = 55, zoom = 4) %>% addTiles() 
  })
}

# Run the application 
shinyApp(ui = ui, server = server)
