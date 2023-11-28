# frozen_string_literal: true

require 'rack/test'
require 'api/base'

RSpec.describe 'Orders API' do # rubocop:disable Metrics/BlockLength
  include Rack::Test::Methods

  before do
    $orders = [] # rubocop:disable Style/GlobalVars
  end

  def app
    API::Base
  end

  let(:due_date) { '2023-05-29' }

  it 'creates a new order' do
    get '/orders'
    expect(last_response.status).to eq(200)
    expect(JSON.parse(last_response.body)).to eq []

    post '/orders', {
      customer_name: 'John Doe',
      total: 100.0,
      due_date:,
      line_items: [
        { name: 'Item 1', quantity: 1, price: 50.0 },
        { name: 'Item 2', quantity: 2, price: 25.0 }
      ]
    }
    expect(last_response.status).to eq(201)
    expect(JSON.parse(last_response.body)).to eq(
      'customer_name' => 'John Doe',
      'total' => 100.0,
      'due_date' => due_date,
      'line_items' => [
        { 'name' => 'Item 1', 'quantity' => 1, 'price' => 50.0 },
        { 'name' => 'Item 2', 'quantity' => 2, 'price' => 25.0 }
      ]
    )

    get '/orders'
    expect(last_response.status).to eq(200)
    expect(JSON.parse(last_response.body).size).to eq(1)
  end

  it 'returns error when creating a new order with invalid params' do
    post '/orders', {
      customer_name: '',
      total: 0.0,
      # due_date: 'the end is near',
      # Here you can't game the system by sending a valid string
      due_date: '7777-77-77',
      line_items: [
        { name: '', quantity: 1, price: 0.0 },
        { name: 'Item 2', quantity: nil, price: 25.0 }
      ]
    }
    expect(last_response.status).to eq(400)
    expect(JSON.parse(last_response.body)).to eq(
      {
        'customer_name' => ['is empty'],
        'due_date' => ['is invalid'],
        'total' => ['does not have a valid value'],
        'line_items[0][name]' => ['is empty'],
        'line_items[1][quantity]' => ['is empty'],
        'line_items[0][price]' => ['does not have a valid value']
      }
    )
  end

  it 'returns error when senfing empty payload' do
    post '/orders', {}
    expect(last_response.status).to eq(400)
    expect(JSON.parse(last_response.body)).to eq(
      {
        'customer_name' => ['is missing', 'is empty'],
        'due_date' => ['is missing', 'is empty'],
        'total' => ['is missing', 'does not have a valid value', 'is empty'],
        'line_items' => ['is missing', 'is empty']
      }
    )
  end
end
