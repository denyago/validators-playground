# frozen_string_literal: true

require_relative '../model/order'
require 'pry'

# rubocop:disable Style/GlobalVars
$orders = []

module API
  class Orders < Grape::API
    format :json

    rescue_from Grape::Exceptions::ValidationErrors do |e|
      error!(e.as_json.map { [_1[:params].first, _1[:messages]] }.to_h, 400)
    end

    resource :orders do
      desc 'Return list of orders'
      get do
        $orders.map(&:to_h)
      end

      desc 'Create a new order'
      params do
        requires :customer_name, type: String, allow_blank: false, desc: 'Customer Name'
        requires :total, type: Float, values: 0.01.., allow_blank: false, desc: 'Total Amount'
        requires :due_date, type: Date, allow_blank: false, desc: 'Due Date'
        requires :line_items, type: Array, allow_blank: false do
          requires :name, type: String, allow_blank: false, desc: 'Name'
          requires :quantity, type: Integer, values: 1.., allow_blank: false, desc: 'Quantity'
          requires :price, type: Float, values: 0.01.., allow_blank: false, desc: 'Price'
        end
      end
      post do
        Order.new(
          customer_name: params[:customer_name],
          total: params[:total],
          due_date: params[:due_date],
          line_items: params[:line_items]
        ).tap { $orders << _1 }.to_h
      end
    end
  end
end

# rubocop:enable Style/GlobalVars
