defmodule PyroTest do
  use ExUnit.Case
  doctest Pyro

  test "greets the world" do
    assert Pyro.hello() == :world
  end
end
