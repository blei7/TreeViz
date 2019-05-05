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
library(lubridate)
library(leaflet.extras)
library(here)


fire_df_heat  <- read_csv(here("data", "combined_data", "fire_heat_final.csv"))
co_data_year  <- read_csv(here("data", "combined_data", "co_final.csv"))
econ_df_year <- read_csv(here("data", "combined_data", "econ_final.csv"))

# fire_df_heat <- fire_df %>%
#   filter(FIRE_YEAR >= 2000) %>%
#   group_by(FIRE_YEAR, LONGITUDE, LATITUDE) %>%
#   summarise(intensity = sum(SIZE_HA))
# 
# co_data_year <- co_data %>%
#   mutate(date = date(date)) %>%
#   mutate(year = year(date)) %>%
#   group_by(year) %>%
#   summarise(avg = mean(ppm)) %>%
#   mutate(avg = as.numeric(avg))
# 
econ_df_year$color <- cut(econ_df_year$dollars,
                          breaks = c(0, 2000000, 4000000, 6000000, 15000000),
                          labels = c("#799385", "#6C8678", "#61786B", "#55685F"))
# 
# 
# econ_df_year <- econ_df %>%
#   filter(Ann?e >= 2000) %>%
#   select(year = Ann?e, dollars = "Dollars (En)")



# Define UI for application that draws a histogram
ui <- fluidPage(
  
  tags$head(
    tags$style(HTML("
      @import url('https://fonts.googleapis.com/css?family=Shadows+Into+Light');
      body {background-color: #F8ECEC; }
    "))
  ),
  
  headerPanel(
    h1("TreeViz", 
       style = "font-family: 'Shadows Into Light', cursive;
        font-weight: 800; line-height: 1.1; 
        color: #382A1F;")),
  

    tags$style(HTML("
                    .tabbable > .nav > li > a[data-value='Fire Zone'] {background-color: #DBAB61;   color:white}
                    .tabbable > .nav > li > a[data-value='Health Impact'] {background-color: #6C8685;  color:white}
                    .tabbable > .nav > li > a[data-value='Economic Impact'] {background-color: #294F50; color:white}
                    ")),  
  
    # Application title
    # headerPanel("TreeViz"),

    # Sidebar with a slider input for number of bins 
        mainPanel(
          tabsetPanel(type = "tabs",
                  tabPanel("Fire Zone",
                           leafletOutput("map1"),
                           #Input: select years
                           wellPanel(style = "background: #f8f2ec",
                             fluidRow(
                               column(12, sliderInput("slider_1",
                                                      label ="Year",
                                                      min = 2000, max = 2017,
                                                      value = 2008,
                                                      step = 1, sep = "")
                               ),
                               column(12,
                                      tagList("Source:", a("BC Historical Fire Incident Locations", href="https://catalogue.data.gov.bc.ca/dataset/fire-incident-locations-historical#edc-pow"))))
                           )
                           ),
                  
                  # Healthcare Panel
                  tabPanel("Health Impact",
                           fluidRow(
                             column(7, leafletOutput("map2")),
                             column(5, plotlyOutput("plot2"))
                           ),
                           wellPanel(style = "background: #f8f2ec",
                             fluidRow(
                               column(12, sliderInput("slider_2",
                                                     label ="Year",
                                                     min = 2000, max = 2017,
                                                     value = 2008,
                                                     step = 1, sep = "")
                                      ),
                               column(12,
                                      tagList("Source:", a("BC Historical Air Quality Data", 
                                                           href="https://catalogue.data.gov.bc.ca/dataset/air-quality-monitoring-unverified-hourly-air-quality-and-meteorological-data")))
                           ))
                  ),
                  tabPanel("Economic Impact", 
                           fluidRow(
                             column(7, leafletOutput("map3")),
                             column(5, plotlyOutput("plot3"))
                           ),
                           wellPanel(style = "background: #f8f2ec",
                             fluidRow(
                               column(12, sliderInput("slider_3",
                                                      label ="Year",
                                                      min = 2000, max = 2017,
                                                      value = 2008,
                                                      step = 1, sep = "")
                               ),
                               tagList("Source:", a("National Forestry Database", 
                                                    href="http://nfdp.ccfm.org/en/data/fires.php")))
                           )
                  )
                  
        ), style = 'width: 1100px')) # ; height: 1000px'))




#------------SERVER-------------#
#-------------------------------#

PROVIDER_MAP = c("OpenMapSurfer.Roads", "Esri.WorldImagery", "Thunderforest.Outdoors")

# Define server logic
server <- function(input, output) {

  # Leaflet Map 1
  filtered_data_map1 <- reactive({fire_df_heat %>%
      filter(FIRE_YEAR == as.numeric(input$slider_1))})
  
  output$map1 <- renderLeaflet({
    leaflet() %>% addProviderTiles(providers$Esri.WorldImagery) %>%
      setView(-120, 55, 4) %>%
      addHeatmap(lng = ~LONGITUDE, lat = ~LATITUDE, intensity = ~intensity,
                 blur = 4, max = 0.01, radius = 2, data = filtered_data_map1())
  })
  
  
  #-------Tab-2-------#
  #-------------------#
  
  
  # Leaflet Map 2
  filtered_data_map2 <- reactive({fire_df_heat %>%
      filter(FIRE_YEAR == as.numeric(input$slider_2))})
  
  output$map2 <- renderLeaflet({
    leaflet() %>% addProviderTiles(providers$Esri.WorldImagery,
                                   options = providerTileOptions(noWrap = TRUE))  %>%
      setView(-120, 55, 4) %>%
      addHeatmap(lng = ~LONGITUDE, lat = ~LATITUDE, intensity = ~intensity,
                 blur = 4, max = 0.01, radius = 2, data = filtered_data_map2())
  })
  
  # Plot 2
  filtered_data_ppm <- reactive({ co_data_year %>%
      filter(year <= as.numeric(input$slider_2))})
  
  output$plot2 <- renderPlotly({
    plot_ly(data = filtered_data_ppm(), x = ~year, y = ~avg, 
            type = "scatter", mode = "markers", color = ~I(color)) %>%
      layout(plot_bgcolor = '#F8ECEC',
             paper_bgcolor = '#F8ECEC',
             yaxis = list(title = "CO PPM per Year", range = c(0, 0.6)),
             xaxis = list(title = "", 
                          range = c(1999, 2019), tickangle = -45, dtick = 3, tick0 = 2000))
  })
  
  #-------Tab-3-------#
  #-------------------#
  
  # Leaflet Map 3
  filtered_data_map3 <- reactive({fire_df_heat %>%
      filter(FIRE_YEAR == as.numeric(input$slider_3))})
  
  output$map3 <- renderLeaflet({
    leaflet() %>% addProviderTiles(providers$Esri.WorldImagery,
                                   options = providerTileOptions(noWrap = TRUE))  %>%
      setView(-120, 55, 4) %>%
      addHeatmap(lng = ~LONGITUDE, lat = ~LATITUDE, intensity = ~intensity,
                 blur = 4, max = 0.01, radius = 2, data = filtered_data_map3())
  })
  
  # Plot 3
  filtered_data_econ <- reactive({ econ_df_year %>%
      filter(year <= as.numeric(input$slider_3))})
  
  output$plot3 <- renderPlotly({
    plot_ly(data = filtered_data_econ(), x = ~year, y = ~dollars, 
            type = "bar", color = ~I(color)) %>%
      layout(font = list(color = "#382A1F"),
             plot_bgcolor = '#F8ECEC',
             paper_bgcolor = '#F8ECEC',
             #title = "Economic Damage over Time",
             yaxis = list(title = "Economic Damage ($)", range = c(0, 13000000)),
             xaxis = list(title = "", 
                          range = c(1999, 2019), tickangle = -45, dtick = 3, tick0 = 2000))
  })
}

# Run the application 
shinyApp(ui = ui, server = server)
