cmake_minimum_required(VERSION 3.27.0)
project(os VERSION 0.9.0 LANGUAGES C)
include_directories(${PROJECT_SOURCE_DIR})
add_link_options("-lbsd")
add_link_options("-lm")
add_link_options("-lpthread")
add_compile_options(
  "$<$<CONFIG:DEBUG>:-fdiagnostics-color=always;-g;-Wall>"
)
add_executable(os shell.c filesystem.c disk.c mem.c asm.c cpu.c)
